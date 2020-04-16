#include <stdlib.h>
#include <string.h>
#include "strops.h"

void str_deep_copy(char *str1, char *str2) {
  char tmparr[strlen(str2)];
  strcpy(tmparr, str2);
  free(str2);
  str1 = tmparr;

  return;
}
