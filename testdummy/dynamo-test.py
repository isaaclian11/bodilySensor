import boto3

dbclient = boto3.client('dynamodb')

print(dbclient.list_tables())

##
response = client.batch_get_item(
    RequestItems={
        'string': {
            'Keys': [
                {
                    'string': {
                        'heartrate': 'int',
                        'Patient': 'string',
                        'B': b'bytes',
                        'username': [
                            'string',
                        ],
                        'speed': [
                            'string',
                        ],
                        'S': [
                            b'bytes',
                        ],
                        'password': {
                            'string': {''}
                        },
                        'L': [
                            {''},
                        ],
                        'NULL': True|False,
                        'BOOL': True|False
                    }
                },
            ],
            'AttributesToGet': [
                'string',
            ],
            'ConsistentRead': True|False,
            'ProjectionExpression': 'string',
            'ExpressionAttributeNames': {
                'string': 'string'
            }
        }
    },
    ReturnConsumedCapacity='INDEXES'|'TOTAL'|'NONE'
)