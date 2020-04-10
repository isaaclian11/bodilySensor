#ifndef JSON_H
#define JSON_H

typedef enum {

  J_PRIMITIVE,
  J_STRING

} Field_t;

typedef struct Field {
  Field_t FIELD_TYPE;
  char *field_name;
} Field;

typedef struct Json {
  // private member vars. pls dont modify outside of member functions
  char *_as_string;
  Field *_fields;
  size_t _size;
  // public member vars.

} Json;

// function defs
Json* json_new(); // ctor
void json_destroy(Json *json); //dtor

void json_add_field(json *json);
void json_remove_field(Json *json);
char* json_get_string(Json *json);

#endif /* JSON_H */
