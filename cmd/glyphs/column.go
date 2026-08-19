package main

import (
	"strings"
	"unicode"
	"unicode/utf8"
)

func PrintColumn(text string, width int) (string, int) {
	var b strings.Builder
	b.Grow(len(text))

	var (
		lines  int // number of lines so far
		rwidth int // running estimated width of the text printed on the terminal
		index  int // the offset into the text
		length int // the last index of a non-whitespace rune found so far
	)

	for {
		if len(text) <= index {
			if index > 0 {
				b.WriteString(text[:index])
				lines++
			}

			return b.String(), lines
		}

		r, w := utf8.DecodeRuneInString(text[index:])

		switch r {
		case '\n':
			if rwidth > 0 {
				index += w
				rwidth++

				b.WriteString(text[:index])
				text = text[index:]

				rwidth, index, length = 0, 0, 0
			} else {
				b.WriteByte('\n')
				text = text[1:]
			}

			lines++
			continue

		default:
			if unicode.IsSpace(r) {
				length = index
			}
		}

		if index >= width {
			var inc int

			if length == 0 {
				length = width
				index = 0
			} else {
				index -= length
				inc = 1
			}

			b.WriteString(text[:length])
			text = text[length+inc:]
			length = 0

			b.WriteByte('\n')
			lines++

			continue
		}

		index += w
		rwidth++ // assumes every rune is 1 character wide
	}
}
