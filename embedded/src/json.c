#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "json.h"

#define INITIAL_SIZE 0

Field* field_new(Field_t type) {
  Field *this = (Field*)malloc(sizeof(Field));
  this->field_type = type;
  this->field_name = "";
  this->field_value = NULL;

  return this;
}

char* field_to_string(Field *field) {
  char *typestr = FIELD_T_STRING[field->field_type];
  char *name = field->field_name;
  char *value;

  // create list of potential var types (not char*)
  int *field_int;

  switch(field->field_type) {
    case J_INT:
      field_int = (int*)(field->field_value);
      char tmp[12];
      sprintf(tmp, "%d", field_int);
      value = tmp;
      break;

    case J_STRING:
      value = (char*)(field->field_value);
      break;
  }

  // size_t result_size = 1 + strlen(typestr) + 2 + strlen(name) + 2 + strlen(value) + 1;
  size_t result_size = strlen(name) + 2 + strlen(value) + 1;

  char result_arr[result_size]; // +1 for null terminator
  // sprintf(result_arr, "(%s) %s: %s", typestr, name, value);
  sprintf(result_arr, "%s: %s", name, value);
  // int i;
  // for (i = 0; i < result_size; i++) {
    // printf("%c", result_arr[i]);
  // }
  char *result = (char*)malloc(sizeof(char) * result_size);
  strcpy(result, result_arr);

  // printf("%s\n", result);

  // must free
  return result;
}

Json* json_new() {

  Json *this = (Json*)malloc(sizeof(Json));

  //member var defs
  this->_as_string = "{}";
  this->_fields = malloc(INITIAL_SIZE * sizeof(Field));
  this->_size = INITIAL_SIZE;

  return this;
}

void json_add_field(Json *json, Field *field) {
  // json->_size += 1;
  // size_t new_size = json->_size * sizeof(Field);
  // json->_fields = (Field*)realloc(json->_fields, new_size);
  // json->_fields[0] = *field;
  //
  // printf("%s\n", json->_fields[0]);
  char *str = field_to_string(field);
  // printf("%d\n", );
}

void json_remove_field(Json *json) {

}

char* json_get_string(Json *json) {
  return json->_as_string;
}

void json_destroy(Json *json) {
  free(json);
}
