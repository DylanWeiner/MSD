#pragma once

////////////////////////////////////////////////////////////////////////
//
// Author:
// Date:
//
// CS 6013
//
// Outline for SerialQueue class.  Fill in the missing data, comments, etc.
//
// Code Completed by Dylan Weiner
// 4/3/26
// Assignment 5 - Thread Safe Queue
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
      current_( new Node{x, tail_} ), size_(sizeof(x) + sizeof(tail_));
   }

   bool dequeue( T * ret ) {
      current_ = head_->next;

      head_->next = current_->next;
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