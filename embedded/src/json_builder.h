#ifndef JSON_BUILDER_H
#define JSON_BUILDER_H

#include "json.h"


typedef struct JsonBuilder {
  // private member vars. pls dont modify outside of member functions
  Json* _json;
  // public member vars.

} JsonBuilder;

// function defs
JsonBuilder* json_builder_new(); // ctor
void json_builder_destroy(JsonBuilder *json_builder); //dtor

Json* json_builder_get_json(JsonBuilder *json_builder);
void json_builder_add_field_string(JsonBuilder *json_builder, char* field_name, Field_t field_type);

#endif /* JSON_BUILDER_H */
