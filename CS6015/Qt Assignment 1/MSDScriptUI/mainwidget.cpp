#include "mainwidget.h"
#include "ui_mainwidget.h"
#include "parse.h"
#include "expr.h"
#include "env.h"


int row = 0;
int col = 0;

mainWidget::mainWidget(QWidget *parent)
    : QWidget(parent)
    , ui(new Ui::mainWidget)
{
    ui->setupUi(this);

    page = new QVBoxLayout;

    expression = new QLabel("Expression:");
    result = new QLabel("Result:");
    expressionText = new QTextEdit();
    resultText = new QTextEdit();

    outputLayout = new QGroupBox();

    interp = new QRadioButton("Interp");
    prettyprint = new QRadioButton("Pretty Print");
    outputButtons = new QVBoxLayout();
    outputButtons->addWidget(interp);
    outputButtons->addWidget(prettyprint);
    outputLayout->setLayout(outputButtons);

    submitButton = new QPushButton("Submit");

    mainText = new QTextEdit();
    resetButton = new QPushButton("Reset");

    setVerticalLayout(page, expression);
    setVerticalLayout(page, expressionText);
    setVerticalLayout(page, outputLayout);
    setVerticalLayout(page, submitButton);
    setVerticalLayout(page, result);
    setVerticalLayout(page, resultText);
    setVerticalLayout(page, resetButton);

    setLayout(page);

    connect(submitButton, &QPushButton::clicked, this, &mainWidget::refreshBox);
    connect(resetButton, &QPushButton::clicked, this, &mainWidget::clearPage);
}

mainWidget::~mainWidget()
{
    delete ui;
}

void mainWidget::refreshBox() {
    std::istringstream str_to_be_parsed(expressionText->toPlainText().toUtf8().constData());
    PTR(Expr) e = parse_expr(str_to_be_parsed);

    if(interp->isChecked()) {
        QString result_to_display = QString::fromStdString(e->interp(Env::empty)->to_string());
        resultText->setText(result_to_display);
    }
    else if(prettyprint->isChecked()) {
        QString result_to_display = QString::fromStdString(e->to_pretty_string());
        resultText->setText(result_to_display);
    }
}

void mainWidget::clearPage() {
    expressionText->clear();
    resultText->clear();
    if(interp->isChecked()) {
        interp->setAutoExclusive(false);
        interp->setChecked(false);
        interp->setAutoExclusive(true);
    }
    else if(prettyprint->isChecked()) {
        prettyprint->setAutoExclusive(false);
        prettyprint->setChecked(false);
        prettyprint->setAutoExclusive(true);
    }
    mainText->clear();
}

void mainWidget::setVerticalLayout(QVBoxLayout *page, QWidget *insertedWidget) {
    page->addWidget(insertedWidget);
}

void mainWidget::addSpace(QGridLayout *divider) {
    divider->addItem(new QSpacerItem(50, 10), 0, 2, 1, 1);
    col++;
}