#!/usr/bin/env bash

# change to script directory
cd "${0%/*}"

cd ..
./gradlew clean shadowJar

cd text-ui-test

# Run the program and redirect output to ACTUAL.TXT
java -jar $(find ../build/libs/ -mindepth 1 -print -quit) < input.txt > ACTUAL.TXT

# Filter out the "remaining" line from both files before comparing.
# This ensures the test doesn't fail just because a day has passed.
grep -v "remaining" ACTUAL.TXT > ACTUAL_FILTERED.TXT
grep -v "remaining" EXPECTED.TXT > EXPECTED_FILTERED.TXT

# Compare the filtered files instead of the raw ones.
# -w ignores all white space (good for Windows vs Mac/Linux issues)
diff -w EXPECTED_FILTERED.TXT ACTUAL_FILTERED.TXT
# --- FIX ENDS HERE ---

if [ $? -eq 0 ]
then
    echo "Test passed!"
    # Clean up temporary filtered files
    rm ACTUAL_FILTERED.TXT EXPECTED_FILTERED.TXT
    exit 0
else
    echo "Test failed!"
    exit 1
fi