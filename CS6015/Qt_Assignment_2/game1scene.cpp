#include "game1scene.h"

game1scene::game1scene(QObject *parent)
    : QGraphicsScene{parent}
{
    cloud1 = new QGraphicsPixmapItem();
    cloud1->setPixmap((QPixmap(":/images/cloud.png")).scaled(300, 100));
    cloud1->setPos(cloud1->x(), cloud1->y());

    cloud2 = new QGraphicsPixmapItem();
    cloud2->setPixmap((QPixmap(":/images/cloud.png")).scaled(300, 100));
    cloud2->setPos(cloud2->x() + 300, cloud2->y());

    cloud3 = new QGraphicsPixmapItem();
    cloud3->setPixmap((QPixmap(":/images/cloud.png")).scaled(300, 100));
    cloud3->setPos(cloud3->x() + 600, cloud3->y());

    collector = new bucket();
    setBackgroundBrush(QBrush(QImage(":/images/background.jpg").scaledToHeight(512).scaledToWidth(910)));
    setSceneRect(0,0,908,510);
    collector->setFlag(QGraphicsItem::ItemIsFocusable);
    collector->setFocus();

    addItem(collector);
    // addItem(rain);
    addItem(cloud1);
    addItem(cloud2);
    addItem(cloud3);

    QTimer *timer_drop = new QTimer(this);
    connect(timer_drop, &QTimer::timeout, this, &game1scene::new_droplet);
    timer_drop->start(1000);
}

void game1scene::new_droplet() {
    water *rain = new water();
    addItem(rain);
    rain->setPos(QRandomGenerator::global()->bounded(908), cloud2->y() + 50);
}