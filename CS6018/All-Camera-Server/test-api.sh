#!/usr/bin/env bash
# End-to-end check of the photo server: accounts, upload, privacy, logout.
# Usage:  bash test-api.sh path/to/photo.jpg [base_url]
# The server must already be running (./gradlew run).

PHOTO="${1:?Usage: bash test-api.sh path/to/photo.jpg [base_url]}"
BASE="${2:-http://localhost:8080}"
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT

SUFFIX="$RANDOM"
ALICE="alice_$SUFFIX"
BOB="bob_$SUFFIX"
PW="correct-horse-1"
PASS=0
FAIL=0

check() { # name expected actual
  if [ "$2" = "$3" ]; then
    echo "PASS  $1 ($3)"; PASS=$((PASS + 1))
  else
    echo "FAIL  $1 (expected $2, got $3)"; FAIL=$((FAIL + 1))
  fi
}

status() { curl -s -o /dev/null -w "%{http_code}" "$@"; }
field() { grep -o "\"$1\":\"[^\"]*\"" | head -1 | cut -d'"' -f4; }
creds() { printf '{"username":"%s","password":"%s"}' "$1" "$2"; }

[ -f "$PHOTO" ] || { echo "Photo not found: $PHOTO"; exit 2; }
[ "$(status "$BASE/health")" = "200" ] || { echo "Server not reachable at $BASE"; exit 2; }

echo "--- accounts"
code=$(curl -s -o "$TMP/alice.json" -w "%{http_code}" -X POST "$BASE/auth/register" \
  -H "Content-Type: application/json" -d "$(creds "$ALICE" "$PW")")
check "register alice" 201 "$code"
ALICE_TOKEN=$(field token < "$TMP/alice.json")
[ -n "$ALICE_TOKEN" ] || { echo "No token returned; stopping."; exit 1; }

check "duplicate username rejected" 409 \
  "$(status -X POST "$BASE/auth/register" -H "Content-Type: application/json" -d "$(creds "$ALICE" "$PW")")"
check "short password rejected" 400 \
  "$(status -X POST "$BASE/auth/register" -H "Content-Type: application/json" -d "$(creds "x_$SUFFIX" "short")")"
check "login with wrong password" 401 \
  "$(status -X POST "$BASE/auth/login" -H "Content-Type: application/json" -d "$(creds "$ALICE" "wrong-password")")"
check "login with right password" 200 \
  "$(status -X POST "$BASE/auth/login" -H "Content-Type: application/json" -d "$(creds "$ALICE" "$PW")")"
check "/auth/me with token" 200 "$(status "$BASE/auth/me" -H "Authorization: Bearer $ALICE_TOKEN")"
check "photos without a token" 401 "$(status "$BASE/photos")"

echo "--- alice's photos"
code=$(curl -s -o "$TMP/up.json" -w "%{http_code}" -X POST "$BASE/photos" \
  -H "Authorization: Bearer $ALICE_TOKEN" -H "Content-Type: image/jpeg" --data-binary @"$PHOTO")
check "alice uploads a photo" 201 "$code"
PHOTO_ID=$(field id < "$TMP/up.json")
[ -n "$PHOTO_ID" ] || { echo "No photo id returned; stopping."; exit 1; }

printf 'this is not a jpeg' > "$TMP/fake.jpg"
check "non-JPEG upload rejected" 415 \
  "$(status -X POST "$BASE/photos" -H "Authorization: Bearer $ALICE_TOKEN" -H "Content-Type: image/jpeg" --data-binary @"$TMP/fake.jpg")"

check "alice's list contains her photo" 1 \
  "$(curl -s "$BASE/photos" -H "Authorization: Bearer $ALICE_TOKEN" | grep -c "$PHOTO_ID")"

code=$(curl -s -o "$TMP/dl.jpg" -w "%{http_code}" "$BASE/photos/$PHOTO_ID/image" \
  -H "Authorization: Bearer $ALICE_TOKEN")
check "alice downloads her photo" 200 "$code"
if cmp -s "$PHOTO" "$TMP/dl.jpg"; then same=identical; else same=different; fi
check "downloaded bytes match the upload" identical "$same"

echo "--- privacy (bob must not see alice's photo)"
code=$(curl -s -o "$TMP/bob.json" -w "%{http_code}" -X POST "$BASE/auth/register" \
  -H "Content-Type: application/json" -d "$(creds "$BOB" "$PW")")
check "register bob" 201 "$code"
BOB_TOKEN=$(field token < "$TMP/bob.json")

check "bob downloads alice's photo" 404 \
  "$(status "$BASE/photos/$PHOTO_ID/image" -H "Authorization: Bearer $BOB_TOKEN")"
check "bob deletes alice's photo" 404 \
  "$(status -X DELETE "$BASE/photos/$PHOTO_ID" -H "Authorization: Bearer $BOB_TOKEN")"
check "bob's list is empty" "[]" \
  "$(curl -s "$BASE/photos" -H "Authorization: Bearer $BOB_TOKEN")"
check "alice's photo survived bob's delete attempt" 200 \
  "$(status "$BASE/photos/$PHOTO_ID/image" -H "Authorization: Bearer $ALICE_TOKEN")"

echo "--- delete and logout"
check "alice deletes her photo" 204 \
  "$(status -X DELETE "$BASE/photos/$PHOTO_ID" -H "Authorization: Bearer $ALICE_TOKEN")"
check "deleted photo is gone" 404 \
  "$(status "$BASE/photos/$PHOTO_ID/image" -H "Authorization: Bearer $ALICE_TOKEN")"
check "alice logs out" 204 \
  "$(status -X POST "$BASE/auth/logout" -H "Authorization: Bearer $ALICE_TOKEN")"
check "old token rejected after logout" 401 \
  "$(status "$BASE/photos" -H "Authorization: Bearer $ALICE_TOKEN")"

echo
echo "$PASS passed, $FAIL failed"
[ "$FAIL" -eq 0 ]