#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "json_builder.h"
#include "libs/strops.h"
#include "json.h"

JsonBuilder* json_builder_new() {

  JsonBuilder *this = (JsonBuilder*)malloc(sizeof(JsonBuilder));

  //member var defs
  this->_json = json_new();

  return this;
}

Json* json_builder_get_json(JsonBuilder *json_builder) {
  return json_builder->_json;
}

// public methods //
void json_builder_add_field(JsonBuilder *json_builder, char *field_name, Field_t field_type) {

  

}

void json_builder_destroy(JsonBuilder *json_builder) {
  free(json_builder);
}
