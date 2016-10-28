rm -rf noteoj_web

CGO_ENABLED=0 go build -a --installsuffix cgo --ldflags="-s" -o noteoj_web
