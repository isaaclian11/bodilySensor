#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "json.h"

#define INITIAL_SIZE 0;

Json* json_new() {

  Json *this = (Json*)malloc(sizeof(Json));

  //member var defs
  this->_as_string = "{}";
  this->_fields = malloc(INITIAL_SIZE * sizeof(Field));
  this->_size = INITIAL_SIZE;

  return this;
}

void json_add_field(Json *json, Field field) {
  json->_size += 1;
  size_t new_size = json->_size * sizeof(Field);
  realloc(json->_fields, new_size);
  json->_fields[0] = field;
}

void json_remove_field(Json *json) {

}

char* json_get_string(Json *json) {
  return json->_as_string;
}

void json_destroy(Json *json) {
  free(json);
}
