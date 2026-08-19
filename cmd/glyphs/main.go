package main

import (
	"fmt"
	"io"
	"os"
	"strings"
	"unsafe"

	"golang.org/x/term"
	"golang.org/x/text/transform"
	"golang.org/x/text/unicode/norm"
)

func main() {
	err := Main(os.Args[1:]...)
	if err != nil {
		fmt.Println(err)
		return
	}
}

func Main(args ...string) error {
	if len(args) == 0 {
		return fmt.Errorf("no command was specified")
	}

	cmd, args := args[0], args[1:]

	switch cmd {
	case "column":
		s, err := inputString(args)
		if err != nil {
			return err
		}

		w, _, err := term.GetSize(int(os.Stdout.Fd()))
		if err != nil {
			return err
		}

		s, _ = PrintColumn(s, w)
		fmt.Println(s)

	case "title":
		s, err := inputString(args)
		if err != nil {
			return err
		}

		m, err := Map(s)
		if err != nil {
			return err
		}

		m = strings.TrimSuffix(m, "\n")
		fmt.Println(m)

	default:
		return fmt.Errorf("unknown command %q\n", cmd)
	}

	return nil
}

func inputString(args []string) (string, error) {
	info, err := os.Stdin.Stat()
	if err != nil {
		return "", err
	}

	var s string
	if (info.Mode() & os.ModeCharDevice) == 0 {
		b, err := io.ReadAll(os.Stdin)
		if err != nil {
			return "", err
		}

		s = unsafe.String(unsafe.SliceData(b), len(b))
	} else {
		s = strings.Join(args, " ")
	}

	s, _, err = transform.String(norm.NFC, s)
	return s, err
}
