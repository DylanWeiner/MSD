#ifndef MAINWIDGET_H
#define MAINWIDGET_H

#include <QWidget>
#include <QLabel>
#include <QLineEdit>
#include <QSpinBox>
#include <QPushButton>
#include <QRadioButton>
#include <QTextEdit>
#include <QVBoxLayout>
#include <QGridLayout>
#include <QSpacerItem>
#include <QGroupBox>

namespace Ui {
class mainWidget;
}

class mainWidget : public QWidget
{
    Q_OBJECT

public:
    explicit mainWidget(QWidget *parent = nullptr);
    ~mainWidget();

    QVBoxLayout *page;

    QLabel *expression;
    QLabel *result;
    QTextEdit *expressionText;
    QTextEdit *resultText;

    QVBoxLayout *outputButtons;
    QGroupBox *outputLayout;

    QLabel *outputTypes;
    QRadioButton *interp;
    QRadioButton *prettyprint;

    QPushButton *submitButton;

    QTextEdit *mainText;
    QPushButton *resetButton;

private:
    Ui::mainWidget *ui;

public slots:
    void setVerticalLayout(QVBoxLayout *page, QWidget *insertedVal);
    void addSpace(QGridLayout *divider);
    void refreshBox();
    void clearPage();
};

#endif // MAINWIDGET_H
