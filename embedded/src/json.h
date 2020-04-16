#ifndef JSON_H
#define JSON_H

#define FOREACH_FIELD_T(FIELD_T) \
        FIELD_T(J_INT)   \
        FIELD_T(J_STRING)  \

#define GENERATE_ENUM(ENUM) ENUM,
#define GENERATE_STRING(STRING) #STRING,

typedef enum {
  FOREACH_FIELD_T(GENERATE_ENUM)
} Field_t;

static const char *FIELD_T_STRING[] = {
  FOREACH_FIELD_T(GENERATE_STRING)
};

typedef struct Field {
  Field_t field_type;
  char *field_name;
  void *field_value;
} Field;

typedef struct Json {
  // private member vars. pls dont modify outside of member functions
  char *_as_string;
  Field *_fields;
  size_t _size;
  // public member vars.

} Json;

Field* field_new(Field_t type);
char* field_to_string(Field *field);

// function defs
Json* json_new(); // ctor
void json_destroy(Json *json); //dtor

void json_add_field(Json *json, Field *field);
void json_remove_field(Json *json);
char* json_get_string(Json *json);

#endif /* JSON_H */
