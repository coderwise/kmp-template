#!/bin/sh
# Creates an Android upload keystore, updates keystore.properties, and prints the base64
# value needed for the KEYSTORE_FILE_BASE64 GitHub Actions secret.

set -e

KEYSTORE_PATH="app/android/upload.keystore"
PROPERTIES_FILE="keystore.properties"
KEY_ALIAS="upload"
VALIDITY_DAYS=9125  # 25 years (Google Play minimum)

# ── Prompt helpers ──────────────────────────────────────────────────────────

prompt() {
  printf "%s: " "$1"
  read -r value
  echo "$value"
}

prompt_default() {
  printf "%s [%s]: " "$1" "$2"
  read -r value
  if [ -z "$value" ]; then echo "$2"; else echo "$value"; fi
}

prompt_password() {
  printf "%s: " "$1"
  stty -echo
  read -r value
  stty echo
  echo ""
  echo "$value"
}

generate_password() {
  LC_ALL=C tr -dc 'A-Za-z0-9@#$%&*+-' < /dev/urandom | head -c 24
}

# ── Gather inputs ────────────────────────────────────────────────────────────

echo ""
echo "=== Android Keystore Generator ==="
echo ""

printf "Keystore password — enter manually or press G to generate: "
read -r password_choice

case "$password_choice" in
  [gG])
    STORE_PASSWORD=$(generate_password)
    echo "Generated password: $STORE_PASSWORD"
    echo "(Save this — it will also be written to $PROPERTIES_FILE)"
    ;;
  *)
    if [ -n "$password_choice" ]; then
      STORE_PASSWORD="$password_choice"
    else
      STORE_PASSWORD=$(prompt_password "Keystore password (min 6 chars)")
    fi
    CONFIRM_PASSWORD=$(prompt_password "Confirm keystore password")
    if [ "$STORE_PASSWORD" != "$CONFIRM_PASSWORD" ]; then
      echo "ERROR: Passwords do not match." >&2
      exit 1
    fi
    ;;
esac

if [ "${#STORE_PASSWORD}" -lt 6 ]; then
  echo "ERROR: Password must be at least 6 characters." >&2
  exit 1
fi

echo ""
echo "Certificate distinguished name (press Enter to skip optional fields):"
FIRST_LAST=$(prompt     "  First and last name")
ORG_UNIT=$(prompt_default "  Organizational unit" "")
ORG=$(prompt_default    "  Organization" "")
CITY=$(prompt_default   "  City / Locality" "")
STATE=$(prompt_default  "  State / Province" "")
COUNTRY=$(prompt_default "  Two-letter country code" "US")

DNAME="CN=${FIRST_LAST}"
[ -n "$ORG_UNIT" ] && DNAME="${DNAME}, OU=${ORG_UNIT}"
[ -n "$ORG"      ] && DNAME="${DNAME}, O=${ORG}"
[ -n "$CITY"     ] && DNAME="${DNAME}, L=${CITY}"
[ -n "$STATE"    ] && DNAME="${DNAME}, ST=${STATE}"
[ -n "$COUNTRY"  ] && DNAME="${DNAME}, C=${COUNTRY}"

# ── Generate keystore ────────────────────────────────────────────────────────

echo ""
echo "Generating keystore at: $KEYSTORE_PATH"

if [ -f "$KEYSTORE_PATH" ]; then
  printf "WARNING: %s already exists. Overwrite? [y/N] " "$KEYSTORE_PATH"
  read -r confirm
  case "$confirm" in
    [yY]*) rm "$KEYSTORE_PATH" ;;
    *) echo "Aborted."; exit 0 ;;
  esac
fi

keytool -genkeypair \
  -keystore "$KEYSTORE_PATH" \
  -storepass "$STORE_PASSWORD" \
  -alias "$KEY_ALIAS" \
  -keypass "$STORE_PASSWORD" \
  -keyalg RSA \
  -keysize 4096 \
  -validity "$VALIDITY_DAYS" \
  -dname "$DNAME"

echo "Keystore created."

# ── Update keystore.properties ───────────────────────────────────────────────

cat > "$PROPERTIES_FILE" <<EOF
storePassword=${STORE_PASSWORD}
keyAlias=${KEY_ALIAS}
EOF

echo "Updated $PROPERTIES_FILE"

# ── Print base64 for GitHub Actions secret ───────────────────────────────────

echo ""
echo "=== GitHub Actions secret ==="
echo "Add the following value as the KEYSTORE_FILE_BASE64 secret:"
echo ""
base64 < "$KEYSTORE_PATH"
echo ""
echo "Also add these secrets:"
echo "  SIGNING_STORE_PASSWORD = ${STORE_PASSWORD}"
echo "  SIGNING_KEY_ALIAS      = ${KEY_ALIAS}"
echo ""
echo "Done."
