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
//     Node(const T & val, T * next) {
//         data = val;
//         next = this->next;
//    }

   SerialQueue() :
      head_( new Node{ T{}, nullptr } ), size_( 0 )
   {
      tail_ = head_;
   }

   void enqueue( const T & x ) {
        // std::cout << "Prev Tail Value: " << tail_->data << std::endl;
        std::cout << "Prev Head Value: " << head_->data << std::endl;
        
        Node* current = new Node{x, nullptr};
        tail_->next = current;
        tail_= current;
        if(size_ == 0) {
            head_ = current;
        }
        size_++;
        
        std::cout << "New Value: " << current->data << std::endl;
    }

    bool dequeue(T * ret) {
        std::cout << "Prev Head Value: " << head_->data << std::endl;
        Node* tmp = head_;
        Node * new_head = tmp->next;
        if(new_head == NULL){
            // head_m.unlock();
            return false; // Nothing in queue
        }
        T value = new_head->data;
        head_ = new_head;
        free( tmp );
        // head_m.unlock();
        std::cout << "New Head Value: " << head_->data << std::endl;
        // *remVal = ret;
        std::cout << "Removed Value: " << *ret << std::endl;
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