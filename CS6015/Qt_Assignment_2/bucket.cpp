#include "bucket.h"

bucket::bucket(QObject *parent):
    QObject{parent}
{
    this->setPixmap((QPixmap(":/images/bucket.png")).scaled(150, 150));
    this->setPos(400, 365);

}

void bucket::keyPressEvent(QKeyEvent *event) {
    if(event->key() == Qt::Key_Right) {
        if(x() + 10 <= 780) {
            this->setPos(x() + 10, y());
        }
        else {
            this->setPos(780, y());
        }
    }
    else if(event->key() == Qt::Key_Left) {
        if(x() - 10 >= -22) {
            this->setPos(x() - 10, y());
        }
        else {
            this->setPos(-22, y());
        }
    }
}