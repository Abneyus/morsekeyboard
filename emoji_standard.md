The morse code standard is based off of the frequencies of symbols in the English language. E being the most common letter is portrayed as a single dot. T being the second most common letter is portrayed as a single dash. Etc. By this logic and using the data from [Emoji Tracker](http://www.emojitracker.com/) we can create a series of codes that place the most common emoji at your fingertips.

The trouble with extending morse code is minimizing conflict with existing conventions. Having been around for nearly 200 years there are sure to be quite a few.

The current emoji implementation is a combination of the E + J character codes and then an appropriate code for each emoji. Ex: If a user wants to input the Face with Tears of Joy emoji the user must input E (.) and J (.---) and then the code assigned to the emoji, in this case (.) so the complete input is (..---.)
