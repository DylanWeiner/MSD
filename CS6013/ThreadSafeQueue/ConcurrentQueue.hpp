#pragma once
#include <iostream>

////////////////////////////////////////////////////////////////////////
//
// Author: Dylan Weiner
// Date:4/3/26
// Assignment 5 - Thread Safe Queue
// CS 6013
//
// Outline for ConcurrentQueue class.
//  
////////////////////////////////////////////////////////////////////////

template <typename T>
class ConcurrentQueue {

public:
   ConcurrentQueue() :
        head_( new Node{ T{}, nullptr } ), size_( 0 )
   {
        tail_ = head_;
   }

   void enqueue( const T & x ) {
        Node* current = new Node{x, nullptr};
        std::mutex tail_m;
        std::unique_lock<std::mutex> taillock(tail_m);
        tail_->next = current;
        tail_= current;
        tail_m.unlock();
        val_enqueued_++;
        size_++;
    }

    bool dequeue(T * ret) {
        std::mutex head_m;
        std::unique_lock<std::mutex> headlock(head_m);
        if(head_ == tail_) {
            head_m.unlock();
            return false; // Nothing in queue
        }
        Node* tmp = head_;
        Node * new_head = tmp->next;
        *ret = new_head->data;
        head_ = new_head;
        head_m.unlock();
        delete tmp;
        
        size_--;
        val_dequeued_++;
        
        return true;
    }

   ~ConcurrentQueue() {

      while( head_ != nullptr ) {
         Node* temp = head_->next;
         delete head_;
         head_ = temp;
      }
   }

   int size_enqueue() const { return val_enqueued_; }
   int size_dequeue() const { return val_dequeued_; }


private:

   struct Node {
      T      data;
      Node * next;
   };

   Node * head_;
   Node * tail_;
   int    val_enqueued_;
   int    val_dequeued_;
   int    size_;
};