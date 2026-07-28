#include "profile.h"
#include "ui_profile.h"

profile::profile(QString str, QWidget *parent)
    : QWidget(parent)
    , ui(new Ui::profile)
{
    ui->setupUi(this);
    ui->label->setText(str);

}

profile::~profile()
{
    delete ui;
}
