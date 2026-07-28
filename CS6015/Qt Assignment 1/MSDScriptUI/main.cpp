#include <QApplication>
#include <QGraphicsView>

#include "mainwidget.h"

int main(int argc, char **argv) {
    QApplication app(argc, argv);

    mainWidget *mainwidget = new class mainWidget();

    mainwidget->show();

    return app.exec();
}