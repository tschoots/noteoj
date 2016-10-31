rm -rf middle_stub

CGO_ENABLED=0 go build -a --installsuffix cgo --ldflags="-s" -o middle_stub
