#include "water.h"
#include "bucket.h"

water::water(QObject *parent):
    QObject{parent}
{
    setPixmap((QPixmap(":/images/water.gif")).scaled(30,30));
    // setPos(400, 0);

    QTimer *timer_drop = new QTimer(this);
    connect(timer_drop, &QTimer::timeout, this, &water::move_droplet);
    timer_drop->start(1000);
}

void water::move_droplet() {
    this->setPos(x(), y() + 40);

    QList<QGraphicsItem*> collisions = collidingItems();
    for (QGraphicsItem *item : collisions) {
        // check if it's colliding with the bucket
        if (dynamic_cast<bucket*>(item)) {
            scene()->removeItem(this);
            delete this;
            return; // must return immediately after deleting this
        }
    }

    if(y() > 510) {
        delete this;
    }
}