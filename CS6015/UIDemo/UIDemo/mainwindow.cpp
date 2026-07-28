#include "mainwindow.h"
#include "./ui_mainwindow.h"
#include "profile.h"

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_pushButton_clicked()
{
    ui->label_3->setText(ui->lineEdit->text() + " " + ui->lineEdit_2->text());
}


void MainWindow::on_verticalScrollBar_valueChanged(int value)
{
    ui->spinBox->setValue(ui->verticalScrollBar->value());
}


void MainWindow::on_spinBox_valueChanged(int arg1)
{
    ui->verticalScrollBar->setValue(ui->spinBox->value());
}


void MainWindow::on_pushButton_2_clicked()
{
    profile *profileWindown = new profile(ui->lineEdit->text());
    profileWindown->show();
    this->close();
}

