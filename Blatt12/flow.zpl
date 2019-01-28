param file := "fluss1.txt";
param n := read file as "1n" use 1 comment "#";

# nodes
set V := {1..n};

# edges
set A := {<i,j> in V*V};

# upper bound for edge flow
param u[A] := read file as "n+" use n skip 1 comment "#";

# flow cost
param c[A] := read file as "n+" use n skip (n + 1) comment "#";

# angebot/Nachfrage
param b[V] := read file as "n+" skip (2 * n + 1) comment "#";

var x[V] integer;

# output
do print A;
do forall <i,j> in A do print "(", i, ", ", j, ") ", c[i,j];
do forall <i> in V do print b[i];

minimize cost: sum <i,j> in A c[i,j] * x[i,j];
subto nodes: #TODO
