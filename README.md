# Introduction
This demo project illustrates how to use Kafka for implementing a message retry mechanism.

Consider the following example of a message named Bill, which includes four fields: id, botId, userId, and amount.

```json
{
"id": "1",
"botId": "1",
"userId": "1",
"amount": 100
}
```
Here, the id is unique and serves as an identifier, while botId is used as a key to ensure the orderliness of message consumption. Note that both botId and userId may appear multiple times in different messages.

### Requirements
The service must ensure messages are consumed in the correct order, guided by the botId.

If a message fails to be consumed, it should be retried multiple times.

Messages that fail to be consumed should be sent to a retry queue.

If a message is set for retry, other messages with the same botId should also be moved to the retry queue to maintain order.

The system should minimize I/O operations.

### Build Instructions

To build the project, run the following command in your terminal:
```
shell
./local_build.sh
```