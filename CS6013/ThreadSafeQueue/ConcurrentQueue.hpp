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
        head_( new Node{ T{}, nullptr } )
   {
        tail_ = head_;
   }

   void enqueue( const T & x ) {
        Node* current = new Node{x, nullptr};
        std::unique_lock<std::mutex> taillock(tail_lock);
        tail_->next = current;
        tail_= current;

        val_enqueued_++;
    }

    bool dequeue(T * ret) {
        std::unique_lock<std::mutex> headlock(head_lock);
        if(head_ == tail_) {
            return false; // Nothing in queue
        }
        Node* tmp = head_;
        Node * new_head = tmp->next;
        *ret = new_head->data;
        head_ = new_head;
        delete tmp;
        
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
   int    val_enqueued_ = 0;
   int    val_dequeued_ = 0;
   std::mutex head_lock;
   std::mutex tail_lock;
};