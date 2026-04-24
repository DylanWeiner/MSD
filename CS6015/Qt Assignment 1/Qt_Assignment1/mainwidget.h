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
    QGridLayout *divider;

    QLabel *fName;
    QLabel *lName;
    QLineEdit *fNameBox;
    QLineEdit *lNameBox;

    QLabel *age;
    QSpinBox *ageSetter;

    QVBoxLayout *genderButtons;
    QGroupBox *genderChecks;

    QLabel *gender;
    QRadioButton *male;
    QRadioButton *female;

    QPushButton *refreshButton;

    QTextEdit *mainText;
    QPushButton *finishButton;

private:
    Ui::mainWidget *ui;

public slots:
    void setGridLayout(QGridLayout *divider, QWidget *insertedVal);
    void setVerticalLayout(QVBoxLayout *page, QWidget *insertedVal);
    void addSpace(QGridLayout *divider);
    void refreshBox();
    void clearPage();
};

#endif // MAINWIDGET_H
