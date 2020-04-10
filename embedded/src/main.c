#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "json_builder.h"
#include "json.h"

int main(void) {
  JsonBuilder *json_builder = json_builder_new();
  Json *json = json_builder_get_json(json_builder);
  printf("%s\n", json_get_string(json));

  json_builder_destroy(json_builder);
}
