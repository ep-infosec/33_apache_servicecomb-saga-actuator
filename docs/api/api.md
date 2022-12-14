# Saga API
### Post transaction and compensation requests to Saga
```
POST /requests
```

####  Description

1. Define requests in order and recovery policy by JSON format as below,put them to body.
```
{
  "policy": "",
  "requests": [
    {
      "id": "",
      "type": "",
      "serviceName": "",
      "parents": [

      ],
      "transaction": {
        "method": "",
        "path": "",
        "retries": "",
        "params": {

        }
      },
      "compensation": {
        "method": "",
        "path": "",
        "retries": "",
        "params": {

        }
      }
    }
  ]
}
```
JSON parameters:
- policy - support `BackwardRecovery` or `ForwardRecovery`.
- requests - transactions array.
  - id - request id. It should be unique among this collection of requests.
  - type - support `rest` for now.
  - serviceName - user-defined service name.
  - parents - request ids. It means this request is only executed after all requests in the parents field are completed.
  - transaction - user-defined transaction that executed by the Saga.
    - method - user-defined, HTTP method.
    - path - user-defined, HTTP path.
    - retries - int, optional, default -1. The max retry times for transaction. If this parameter is less than or equal to 0, transaction will retry infinitely.
    - params - support `form`,`json`,`body`,`query`.
  - compensation - user-defined compensation that executed by the Saga.
    - method - user-defined, HTTP method.
    - path - user-defined, HTTP path.
    - retries - int, optional, default 3. The max retry times for compensation.
    - params - support `form`,`json`,`body`,`query`.

2. Set content type to `text/plain`.

3. Send them to Saga service.

####  Example request
```
curl -XPOST -H "Content-Type: text/plain" -d @./request.json  http://<docker.host.ip:saga.port>/requests
```

####  Example response
```
success
```

####  Status codes
-   **200** – no error
-   **400** – bad parameter
-   **500** – server error


### Get all the Saga events
```
GET /events
```

####  Description
Get all the Saga events.

####  Example request
```
curl -XGET http://<docker.host.ip:saga.port>/events
```

####  Example response
```
{
    "88658e73-eff5-4d31-887e-019201d6b560": [
        {
            "id": 1,
            "sagaId": "88658e73-eff5-4d31-887e-019201d6b560",
            "creationTime": "2017-09-15T01:15:40Z",
            "type": "SagaStartedEvent",
            "contentJson": "{\"policy\": \"BackwardRecovery\", \"requests\": [{\"id\": \"request-car\", \"type\": \"rest\", \"serviceName\": \"car-rental-service\", \"transaction\": {\"path\": \"/rentals\", \"method\": \"post\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}, \"compensation\": {\"path\": \"/rentals\", \"method\": \"put\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}}, {\"id\": \"request-hotel\", \"type\": \"rest\", \"serviceName\": \"hotel-reservation-service\", \"transaction\": {\"path\": \"/reservations\", \"method\": \"post\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}, \"compensation\": {\"path\": \"/reservations\", \"method\": \"put\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}}, {\"id\": \"request-flight\", \"type\": \"rest\", \"serviceName\": \"flight-booking-service\", \"transaction\": {\"path\": \"/bookings\", \"method\": \"post\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}, \"compensation\": {\"path\": \"/bookings\", \"method\": \"put\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}}, {\"id\": \"request-payment\", \"type\": \"rest\", \"parents\": [\"request-car\", \"request-flight\", \"request-hotel\"], \"serviceName\": \"payment-service\", \"transaction\": {\"path\": \"/payments\", \"method\": \"post\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}, \"compensation\": {\"path\": \"/payments\", \"method\": \"put\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}}]}"
        },
        {
            "id": 2,
            "sagaId": "88658e73-eff5-4d31-887e-019201d6b560",
            "creationTime": "2017-09-15T01:15:40Z",
            "type": "TransactionStartedEvent",
            "contentJson": "{\"id\": \"request-flight\", \"type\": \"rest\", \"parents\": [], \"fallback\": {\"type\": \"NOP\"}, \"serviceName\": \"flight-booking-service\", \"transaction\": {\"path\": \"/bookings\", \"method\": \"post\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}, \"compensation\": {\"path\": \"/bookings\", \"method\": \"put\", \"params\": {\"form\": {\"customerId\": \"mike\"}}, \"retries\": 3}}"
        },
        {
            "id": 3,
            "sagaId": "88658e73-eff5-4d31-887e-019201d6b560",
            "creationTime": "2017-09-15T01:15:40Z",
            "type": "TransactionStartedEvent",
            "contentJson": "{\"id\": \"request-car\", \"type\": \"rest\", \"parents\": [], \"fallback\": {\"type\": \"NOP\"}, \"serviceName\": \"car-rental-service\", \"transaction\": {\"path\": \"/rentals\", \"method\": \"post\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}, \"compensation\": {\"path\": \"/rentals\", \"method\": \"put\", \"params\": {\"form\": {\"customerId\": \"mike\"}}, \"retries\": 3}}"
        },
        {
            "id": 4,
            "sagaId": "88658e73-eff5-4d31-887e-019201d6b560",
            "creationTime": "2017-09-15T01:15:40Z",
            "type": "TransactionStartedEvent",
            "contentJson": "{\"id\": \"request-hotel\", \"type\": \"rest\", \"parents\": [], \"fallback\": {\"type\": \"NOP\"}, \"serviceName\": \"hotel-reservation-service\", \"transaction\": {\"path\": \"/reservations\", \"method\": \"post\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}, \"compensation\": {\"path\": \"/reservations\", \"method\": \"put\", \"params\": {\"form\": {\"customerId\": \"mike\"}}, \"retries\": 3}}"
        },
        {
            "id": 5,
            "sagaId": "88658e73-eff5-4d31-887e-019201d6b560",
            "creationTime": "2017-09-15T01:15:40Z",
            "type": "TransactionEndedEvent",
            "contentJson": "{\"request\": {\"id\": \"request-flight\", \"type\": \"rest\", \"parents\": [], \"fallback\": {\"type\": \"NOP\"}, \"serviceName\": \"flight-booking-service\", \"transaction\": {\"path\": \"/bookings\", \"method\": \"post\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}, \"compensation\": {\"path\": \"/bookings\", \"method\": \"put\", \"params\": {\"form\": {\"customerId\": \"mike\"}}, \"retries\": 3}}, \"response\": {\"body\": \"{\\n  \\\"statusCode\\\": 200,\\n  \\\"content\\\": \\\"Flight booked with id 5b3c462a-b5d4-45b8-b5e4-8c9aa7d1c069 for customer mike\\\"\\n}\"}}"
        },
        {
            "id": 6,
            "sagaId": "88658e73-eff5-4d31-887e-019201d6b560",
            "creationTime": "2017-09-15T01:15:40Z",
            "type": "TransactionEndedEvent",
            "contentJson": "{\"request\": {\"id\": \"request-hotel\", \"type\": \"rest\", \"parents\": [], \"fallback\": {\"type\": \"NOP\"}, \"serviceName\": \"hotel-reservation-service\", \"transaction\": {\"path\": \"/reservations\", \"method\": \"post\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}, \"compensation\": {\"path\": \"/reservations\", \"method\": \"put\", \"params\": {\"form\": {\"customerId\": \"mike\"}}, \"retries\": 3}}, \"response\": {\"body\": \"{\\n  \\\"statusCode\\\": 200,\\n  \\\"content\\\": \\\"Hotel reserved with id eb2366e1-411d-4352-84fb-6b5ab446ec81 for customer mike\\\"\\n}\"}}"
        },
        {
            "id": 7,
            "sagaId": "88658e73-eff5-4d31-887e-019201d6b560",
            "creationTime": "2017-09-15T01:15:41Z",
            "type": "TransactionEndedEvent",
            "contentJson": "{\"request\": {\"id\": \"request-car\", \"type\": \"rest\", \"parents\": [], \"fallback\": {\"type\": \"NOP\"}, \"serviceName\": \"car-rental-service\", \"transaction\": {\"path\": \"/rentals\", \"method\": \"post\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}, \"compensation\": {\"path\": \"/rentals\", \"method\": \"put\", \"params\": {\"form\": {\"customerId\": \"mike\"}}, \"retries\": 3}}, \"response\": {\"body\": \"{\\n  \\\"statusCode\\\": 200,\\n  \\\"content\\\": \\\"Car rented with id 3c22da64-d4ac-4870-b9bb-54b603721925 for customer mike\\\"\\n}\"}}"
        },
        {
            "id": 8,
            "sagaId": "88658e73-eff5-4d31-887e-019201d6b560",
            "creationTime": "2017-09-15T01:15:41Z",
            "type": "TransactionStartedEvent",
            "contentJson": "{\"id\": \"request-payment\", \"type\": \"rest\", \"parents\": [\"request-car\", \"request-flight\", \"request-hotel\"], \"fallback\": {\"type\": \"NOP\"}, \"serviceName\": \"payment-service\", \"transaction\": {\"path\": \"/payments\", \"method\": \"post\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}, \"compensation\": {\"path\": \"/payments\", \"method\": \"put\", \"params\": {\"form\": {\"customerId\": \"mike\"}}, \"retries\": 3}}"
        },
        {
            "id": 9,
            "sagaId": "88658e73-eff5-4d31-887e-019201d6b560",
            "creationTime": "2017-09-15T01:15:41Z",
            "type": "TransactionEndedEvent",
            "contentJson": "{\"request\": {\"id\": \"request-payment\", \"type\": \"rest\", \"parents\": [\"request-car\", \"request-flight\", \"request-hotel\"], \"fallback\": {\"type\": \"NOP\"}, \"serviceName\": \"payment-service\", \"transaction\": {\"path\": \"/payments\", \"method\": \"post\", \"params\": {\"form\": {\"customerId\": \"mike\"}}}, \"compensation\": {\"path\": \"/payments\", \"method\": \"put\", \"params\": {\"form\": {\"customerId\": \"mike\"}}, \"retries\": 3}}, \"response\": {\"body\": \"{\\n  \\\"statusCode\\\": 200,\\n  \\\"content\\\": \\\"Payment made for customer mike and remaining balance is 200\\\"\\n}\"}}"
        },
        {
            "id": 10,
            "sagaId": "88658e73-eff5-4d31-887e-019201d6b560",
            "creationTime": "2017-09-15T01:15:41Z",
            "type": "SagaEndedEvent",
            "contentJson": "{}"
        }
    ]
}
```

####  Status codes
-   **200** – no error

### Invoke embedded Saga to execute SQL transaction

In order to Invoke embedded saga, it is necessary to add dependencies to your applications.

There are a `maven` example

```
<dependencies>

    ...
    
    <dependency>
        <groupId>org.apache.servicecomb.saga</groupId>
        <artifactId>saga-core</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.servicecomb.saga</groupId>
        <artifactId>saga-format</artifactId>
    </dependency>
    
    ...
    
</dependencies>
```

####  Description
    
1. Implement SQLTransport interface in your own applications.

2. Instance `SagaExecutionComponent` and inject it into your own applications.

3. Define requests in order and recovery policy by JSON format as below in your applications.

```
{
  "policy": "",
  "requests": [
    {
      "id": "",
      "type": "",
      "datasource": "",
      "parents": [

      ],
      "transaction": {
        "sql": "",
        "retries": "",
        "params": [
          []
        ]
      },
      "compensation": {
        "sql": "",
        "retries": "",
        "params": [
          []
        ]
      }
    }
  ]
}
```
JSON parameters:
- policy - support `BackwardRecovery` or `ForwardRecovery`.
- requests - transactions array.
  - id - request id. It should be unique among this collection of requests.
  - type - support `sql` for now.
  - datasource - user-defined datasource name.
  - parents - request ids. It means this request is only executed after all requests in the parents field are completed.
  - transaction - user-defined transaction that executed by the Saga.
    - sql - user-defined, forward sql.
    - retries - int, optional, default -1. The max retry times for transaction. If this parameter is less than or equal to 0, transaction will retry infinitely.
    - params - parameters for forward sql.
  - compensation - user-defined compensation that executed by the Saga.
    - sql - user-defined, backward sql.
    - retries - int, optional, default 3. The max retry times for compensation.
    - params - parameters for backward sql.

4. Invoke `SagaExecutionComponent.run(String json)` function to execute saga.

#### Example Implement SQLTransport interface

```
public class ExampleSQLTransport implements SQLTransport {
  @Override
  public SagaResponse with(final String datasource, final String sql, final List<String> params) {
    try {
      // invoke your own code to execute sql.
    } catch (Exception e) {
      throw new TransportFailedException("execute SQL " + sql + " occur exception: ", e);
    }
    return new JsonSuccessfulSagaResponse("{}");
  }
}
```

#### Example Instance `SagaExecutionComponent`

```
  private final SQLTransport sqlTransport = new ExampleSQLTransport;

  private final TransportFactory<SQLTransport> transportFactory = new TransportFactory<SQLTransport>() {
    @Override
    public SQLTransport getTransport() {
      return sqlTransport;
    }
  };

  private final SagaExecutionComponent coordinator = new SagaExecutionComponent(
      new EmbeddedPersistentStore(),
      new JacksonFromJsonFormat(transportFactory),
      null,
      new GraphBasedSagaFactory(500, eventStore, new ChildrenExtractor(), Executors.newFixedThreadPool(5))
  );
```

