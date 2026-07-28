#pragma once
#include <iostream>

////////////////////////////////////////////////////////////////////////
//
// Author: Dylan Weiner
// Date:4/3/26
// Assignment 5 - Thread Safe Queue
// CS 6013
//
// Outline for SerialQueue class.
//  
////////////////////////////////////////////////////////////////////////

template <typename T>
class SerialQueue {

public:
   SerialQueue() :
        head_( new Node{ T{}, nullptr } ), size_( 0 )
   {
        tail_ = head_;
   }

   void enqueue( const T & x ) {
        Node* current = new Node{x, nullptr};
        tail_->next = current;
        tail_= current;
        size_++;
    }

    bool dequeue(T * ret) {
        if(head_ == tail_) {
            return false; // Nothing in queue
        }
        Node* tmp = head_;
        Node * new_head = tmp->next;
        *ret = new_head->data;
        head_ = new_head;
        delete tmp;
        // head_m.unlock();
        
        size_--;
        return true;
    }

   ~SerialQueue() {

      while( head_ != nullptr ) {
         Node* temp = head_->next;
         delete head_;
         head_ = temp;
      }
   }

   int size() const { return size_; }


private:

   struct Node {
      T      data;
      Node * next;
   };

   Node * head_;
   Node * tail_;
   int    size_;
};