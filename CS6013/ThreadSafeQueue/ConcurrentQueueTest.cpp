// Dylan Weiner
// 4/3/26
// Assignment 5 - Thread Safe Queue
// CS 6013

#include "ConcurrentQueue.hpp"
#include <thread>
#include <assert.h>
#include <set>

bool testProduceFirstQueue( int num_producer, int num_consumer, int num_ints ) {
    ConcurrentQueue<int> exQueue;
    int total_items = (num_producer - num_consumer)*num_ints;
    std::vector<std::thread> producers;
    std::vector<std::thread> consumers;
    std::vector<std::vector<int>> consumed_items(num_consumer);
    std::vector<int> fails(num_consumer, 0);

    for(int i = 0; i < num_producer; i++) {
        producers.emplace_back( 
            [&exQueue, num_ints, i](){
            for(int j = num_ints * i; j < (num_ints * (i + 1)); j++) {
                exQueue.enqueue(j);
            }
        });
    }

    for(int i = 0; i < num_consumer; i++) {
        consumers.emplace_back([&exQueue, num_ints, i, &consumed_items, &fails](){
            int ret;
            for(int j = 0; j < num_ints; j++) {
                if (exQueue.dequeue(&ret) == false) {
                    fails[i]++;
                } else {
                    consumed_items[i].push_back(ret);
                }
            }
        });
    }       

    for(auto & producer : producers) {
        producer.join();
    }

    for(auto & consumer : consumers) {
        consumer.join();
    }

    std::set <int> unique_items;
    
    for(int i = 0; i < num_consumer; i++) {
        for(int j = 0; j < consumed_items[i].size(); j++) {
            if(unique_items.find(consumed_items[i][j]) == unique_items.end()) {
                unique_items.insert(consumed_items[i][j]);
            }
            else {
                std::cout << "Duplicate item found: " << consumed_items[i][j] << std::endl;
            }
        }
    }

    int actual_items = (exQueue.size_enqueue() - exQueue.size_dequeue());

    std::cout << "Total Items: " << total_items << std::endl;
    std::cout << "Remaining Items: " << actual_items << std::endl;

    int total_fails = 0;
    for(int i = 0; i < num_consumer; i++) {
        std::cout << "Consumer " << i << " failed to dequeue " << fails[i] << " times." << std::endl;
        total_fails += fails[i];
    }
    
    if((total_items + total_fails) == actual_items) {
        return true;
    }
    return false;
}

bool testConsumeFirstQueue( int num_producer, int num_consumer, int num_ints ) {
    ConcurrentQueue<int> exQueue;
    int total_items = (num_producer - num_consumer)*num_ints;
    std::vector<std::thread> producers;
    std::vector<std::thread> consumers;
    std::vector<std::vector<int>> consumed_items(num_consumer);
    std::vector<int> fails(num_consumer, 0);

    for(int i = 0; i < num_consumer; i++) {
        consumers.emplace_back([&exQueue, num_ints, i, &consumed_items, &fails](){
            int ret;
            for(int j = 0; j < num_ints; j++) {
                if (exQueue.dequeue(&ret) == false) {
                    fails[i]++;
                } else {
                    consumed_items[i].push_back(ret);
                }
            }
        });
    }

    for(int i = 0; i < num_producer; i++) {
        producers.emplace_back( 
            [&exQueue, num_ints, i](){
            for(int j = num_ints * i; j < (num_ints * (i + 1)); j++) {
                exQueue.enqueue(j);
            }
        });
    }       

    for(auto & producer : producers) {
        producer.join();
    }

    for(auto & consumer : consumers) {
        consumer.join();
    }

    std::set <int> unique_items;
    
    for(int i = 0; i < num_consumer; i++) {
        for(int j = 0; j < consumed_items[i].size(); j++) {
            if(unique_items.find(consumed_items[i][j]) == unique_items.end()) {
                unique_items.insert(consumed_items[i][j]);
            }
            else {
                std::cout << "Duplicate item found: " << consumed_items[i][j] << std::endl;
            }
        }
    }

    int actual_items = (exQueue.size_enqueue() - exQueue.size_dequeue());

    std::cout << "Total Items: " << total_items << std::endl;
    std::cout << "Remaining Items: " << actual_items << std::endl;

    int total_fails = 0;
    for(int i = 0; i < num_consumer; i++) {
        std::cout << "Consumer " << i << " failed to dequeue " << fails[i] << " times." << std::endl;
        total_fails += fails[i];
    }
    
    if((total_items + total_fails) == actual_items) {
        return true;
    }
    return false;
}

int main( int argc, char **argv ) {
    assert(argc == 4);
    assert(testConsumeFirstQueue(std::stoi(argv[1]), std::stoi(argv[2]), std::stoi(argv[3])));
    assert(testProduceFirstQueue(std::stoi(argv[1]), std::stoi(argv[2]), std::stoi(argv[3])));
    std::cout << "Test passed!" << std::endl;
    return 0;
}