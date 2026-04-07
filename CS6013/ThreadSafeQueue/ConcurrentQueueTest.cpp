#include "ConcurrentQueue.hpp"
#include <thread>
#include <assert.h>

bool testQueue( int num_producer, int num_consumer, int num_ints ) {
    ConcurrentQueue<int> exQueue;
    int total_threads = (num_producer - num_consumer)*num_ints;
    std::vector<std::thread> producers;
    std::vector<std::thread> consumers;

    for(int i = 0; i < num_producer; i++) {
        producers.emplace_back([&exQueue, num_ints](){
            for(int j = 0; j < num_ints; j++) {
                exQueue.enqueue(j);
            }
        });
    }

    for(int i = 0; i < num_consumer; i++) {
        consumers.emplace_back([&exQueue, num_ints](){
            int ret;
            for(int j = 0; j < num_ints; j++) {
                while(!exQueue.dequeue(&ret)) {
                    // spin until we can dequeue
                }
                assert(ret == j);
            }
        });
    }

    for(auto & producer : producers) {
        producer.join();
    }

    for(auto & consumer : consumers) {
        consumer.join();
    }

    int actual_threads = (exQueue.size_enqueue() - exQueue.size_dequeue());

    std::cout << "Total threads: " << total_threads << std::endl;
    std::cout << "Actual threads: " << actual_threads << std::endl;

    if(total_threads == actual_threads) {
        return true;
    }
    return false;
}

int main( int argc, char **argv ) {
    assert(testQueue(std::stoi(argv[1]), std::stoi(argv[2]), std::stoi(argv[3])));
    std::cout << "Test passed!" << std::endl;
    return 0;
}