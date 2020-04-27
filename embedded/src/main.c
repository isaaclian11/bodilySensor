#include <stdio.h>
#include <stdlib.h>
#include <string.h>
// #include "json_builder.h"
#include "json.h"

int main(void) {
  // JsonBuilder *json_builder = json_builder_new();
  // Json *json = json_builder_get_json(json_builder);
  // Json *json = json_new();
  // printf("%s\n", json_get_string(json));

  char *field_string;

  Field *f1 = field_new(J_INT);
  f1->field_name = "BPM";
  f1->field_value = (int*)56;

  field_string = field_to_string(f1);
  char tmp1arr[strlen(field_string)];
  strcpy(tmp1arr, field_string);
  free(field_string);
  char *str1 = tmp1arr;

  Field *f2 = field_new(J_STRING);
  f2->field_name = "Patient Name";
  f2->field_value = (char*)"Mario Andretti";

  field_string = field_to_string(f2);
  char tmp2arr[strlen(field_string)];
  strcpy(tmp2arr, field_string);
  free(field_string);
  char *str2 = tmp2arr;

  Field *f3 = field_new(J_INT);
  f3->field_name = "Collection Period (ms)";
  f3->field_value = (int*)20000;

  field_string = field_to_string(f3);
  char tmp3arr[strlen(field_string)];
  strcpy(tmp3arr, field_string);
  free(field_string);
  char *str3 = tmp3arr;

  printf("%s\n%s\n%s\n", str2, str1, str3);

  free(f2);
  free(f1);
  free(f3);
  // printf("%s\n", str1);

  // json_add_field(json, tmpfield);

  // json_destroy(json);
  // json_builder_destroy(json_builder);
}
