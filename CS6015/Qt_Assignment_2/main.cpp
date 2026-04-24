#include <QGraphicsView>
#include <QApplication>
#include "game1scene.h"

int main(int argc, char **argv) {
    QApplication app(argc, argv);

    game1scene *scene1 = new game1scene();

    QGraphicsView view;

    view.setScene(scene1);
    view.show();
    view.setFixedSize(910, 512);
    view.setHorizontalScrollBarPolicy((Qt::ScrollBarAlwaysOff));
    view.setVerticalScrollBarPolicy((Qt::ScrollBarAlwaysOff));

    return app.exec();

}