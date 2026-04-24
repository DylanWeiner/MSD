#ifndef GAME1SCENE_H
#define GAME1SCENE_H

#include <QGraphicsScene>
#include <QGraphicsPixmapItem>
#include <QRandomGenerator>
#include "bucket.h"
#include "water.h"

class game1scene : public QGraphicsScene
{
    Q_OBJECT

private:
    bucket *collector;
    water *rain;
    QGraphicsPixmapItem *cloud1;
    QGraphicsPixmapItem *cloud2;
    QGraphicsPixmapItem *cloud3;

public:
    explicit game1scene(QObject *parent = nullptr);
    void new_droplet();

};

#endif //
