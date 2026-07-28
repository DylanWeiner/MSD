#include "mainScene.h"
#include "man.h"

mainScene::mainScene() {
    setBackgroundBrush(QPixmap("").scaled(300, 300));
    setSceneRect(0, 0, 300, 300);


    man *manObj = new man();
    addItem(manObj);
}
