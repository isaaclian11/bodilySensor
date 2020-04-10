INCL=./libs

CC = gcc
CFLAGS=-I. -I$(INCL)
VPATH=./:$(INCL)

MEMLOG=debug.log

ODIR=./obj

DEPS = json_builder.h strops.h json.h

_OBJ = main.o json_builder.o strops.o json.o
OBJ = $(patsubst %,$(ODIR)/%,$(_OBJ))

$(ODIR)/%.o: %.c $(DEPS)
	$(CC) -c -o $@ $< $(CFLAGS)

main: $(OBJ)
	$(CC) -o $@ $^ $(CFLAGS)

.PHONY: clean

clean:
	rm $(ODIR)/*.o core

memtest:
	rm $(MEMLOG)
	valgrind ./main &> $(MEMLOG)

run:
	./main
