#ifndef WATER_H
#define WATER_H

#include <QObject>
#include <QGraphicsPixmapItem>
#include <QTimer>
#include <QGraphicsScene>


class water : public QObject, public QGraphicsPixmapItem
{
    Q_OBJECT
public:
    explicit water(QObject *parent = nullptr);

signals:

public slots:
    void move_droplet();
};

#endif //
