#include "mainwidget.h"
#include "ui_mainwidget.h"

int row = 0;
int col = 0;

mainWidget::mainWidget(QWidget *parent)
    : QWidget(parent)
    , ui(new Ui::mainWidget)
{
    ui->setupUi(this);

    page = new QVBoxLayout;
    divider = new QGridLayout;

    fName = new QLabel("First Name: ");
    lName = new QLabel("Last Name: ");
    fNameBox = new QLineEdit();
    lNameBox = new QLineEdit();

    genderChecks = new QGroupBox();

    age = new QLabel("Age: ");
    ageSetter = new QSpinBox();

    gender = new QLabel("Gender: ");

    male = new QRadioButton("Male");
    female = new QRadioButton("Female");
    genderButtons = new QVBoxLayout();
    genderButtons->addWidget(male);
    genderButtons->addWidget(female);
    genderChecks->setLayout(genderButtons);

    refreshButton = new QPushButton("Refresh");

    mainText = new QTextEdit();
    finishButton = new QPushButton("Finish");

    setGridLayout(divider, fName);
    setGridLayout(divider, fNameBox);
    setGridLayout(divider, age);
    setGridLayout(divider, ageSetter);
    setGridLayout(divider, lName);
    setGridLayout(divider, lNameBox);
    addSpace(divider);
    addSpace(divider);
    setGridLayout(divider, gender);
    setGridLayout(divider, genderChecks);
    addSpace(divider);
    addSpace(divider);
    setGridLayout(divider, refreshButton);

    page->addLayout(divider);
    setVerticalLayout(page, mainText);
    setVerticalLayout(page, finishButton);

    setLayout(page);



    connect(refreshButton, &QPushButton::clicked, this, &mainWidget::refreshBox);
    connect(finishButton, &QPushButton::clicked, this, &mainWidget::clearPage);
}

mainWidget::~mainWidget()
{
    delete ui;
}

void mainWidget::refreshBox() {

    QString firstName = fNameBox->text();
    QString lastName = lNameBox->text();
    QString age = ageSetter->text();
    QString value = "";
    if(male->isChecked()) {
        value = male->text();
    }
    else if(female->isChecked()) {
        value = female->text();
    }
    mainText->setText("First Name: " + firstName + "\nLast Name: " + lastName + "\nAge: " + age + "\nGender: " + value);
}

void mainWidget::clearPage() {
    fNameBox->clear();
    lNameBox->clear();
    ageSetter->clear();
    if(male->isChecked()) {
        male->setAutoExclusive(false);
        male->setChecked(false);
        male->setAutoExclusive(true);
    }
    else if(female->isChecked()) {
        female->setAutoExclusive(false);
        female->setChecked(false);
        female->setAutoExclusive(true);
    }
    mainText->clear();
}

void mainWidget::setGridLayout(QGridLayout *divider, QWidget *insertedVal) {
    if(col < 4) {
        divider->addWidget(insertedVal, row, col);
        col++;
    } else {
        col = 0;
        row++;
        divider->addWidget(insertedVal, row, col);
        col++;
    }
}

void mainWidget::setVerticalLayout(QVBoxLayout *page, QWidget *insertedWidget) {
    page->addWidget(insertedWidget);
}

void mainWidget::addSpace(QGridLayout *divider) {
    divider->addItem(new QSpacerItem(50, 10), 0, 2, 1, 1);
    col++;
}
