#!/bin/python
from cryptography.hazmat.primitives import serialization
import jwt
import datetime
import argparse

def open_private_cert(filepath="private.pem"):
    private_key = ""
    filepath = filepath or "private.pem"

    with open(filepath, "rb") as f:
        private_key = serialization.load_pem_private_key(
            f.read(),
            password=None
        )

    return private_key

def generate_token(user_id, private_key):
    payload = {
        "sub": user_id,
        "user_id": user_id,
        "iat": datetime.datetime.now(datetime.UTC),
        "exp": datetime.datetime.now(datetime.UTC) + datetime.timedelta(hours=24)
    }

    token = jwt.encode(
        payload,
        private_key,
        algorithm="RS256"
    )

    return token


def main():
    parser = argparse.ArgumentParser(
                        prog='JWT signer',
                        description='Sign a JWT based on private key file.')

    parser.add_argument('user_id')
    parser.add_argument('-c', '--certfile')
    parser.add_argument('-o', '--output')

    args = parser.parse_args()

    try:
        key = open_private_cert(args.certfile)
    except FileNotFoundError as e:
        print(e)
        return

    token = generate_token(args.user_id, key)
    if (args.output != None):
        with open(args.output, "w") as file:
            file.write(token)
    else:
        print(token)

if __name__ == "__main__":
    main()
