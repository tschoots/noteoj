package main

import (
    "fmt"
    "net/http"
    //"os/exec"
    //"os"
)



func handler(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintf(w, "Hi there!\n\n")
    
    //cmd := exec.Command("java",
    //                    "-version")
	//fmt.Println(cmd.Args)
	//cmd.Stdout = os.Stdout
	//cmd.Stderr = os.Stderr
	
	//cmd.Stdout = w
	//cmd.Stderr = w
	
	//go io.Copy(log., os.Stderr)
	//err := cmd.Run()
	//if err != nil {
	//	fmt.Println(err.Error())
	//	//return false, err
	//}
}

func main() {
    http.HandleFunc("/", handler)
    if err := http.ListenAndServe(":8080", nil); err != nil {
    	fmt.Printf("error : %s\n", err)
    }
}
