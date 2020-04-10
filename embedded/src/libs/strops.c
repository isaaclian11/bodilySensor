#include <stdlib.h>
#include <string.h>
#include "strops.h"

// must free result after return
char* concat(char* str1, char* str2) {
  int result_length = strlen(str1) + strlen(str2);
  char *result = malloc(result_length);

  strncpy(result, str1, sizeof(result));
  strncat(result, str2, (sizeof(result) - strlen(result)));

  return result;
}

// must free result after return
char* insert(char *str1, int index, char *str2) {
  int result_length = strlen(str1) + strlen(str2);
  char *result = malloc(result_length);


}
