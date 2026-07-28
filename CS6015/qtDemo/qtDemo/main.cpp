#include <QApplication>
#include <QGraphicsView>

#include "mainScene.h"

int main(int argc, char **argv) {
    QApplication app(argc, argv);

    //create scene
    // QGraphicsScene *mainscene = new QGraphicsScene();
    // //create an image item
    // QGraphicsPixmapItem *imageItem = new QGraphicsPixmapItem();

    // imageItem->setPixmap(QPixmap("://Images/5_heart.png").scaled(200, 200));

    // //add the item to the scene
    // mainscene->addItem(imageItem);

    mainScene *mainscene = new mainScene();

    // create a view
    QGraphicsView *mainView = new QGraphicsView();

    // add the scene to the view
    mainView->setScene(mainscene);

    // show the view
    mainView->show();

    return app.exec();
}