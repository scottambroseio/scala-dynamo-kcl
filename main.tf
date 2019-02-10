provider "aws" {
  access_key = "${var.access_key}"
  secret_key = "${var.secret_key}"
  region     = "${var.region}"
}

resource "aws_dynamodb_table" "table" {
  name             = "${var.table_name}"
  billing_mode     = "PROVISIONED"
  read_capacity    = "${var.rcus}"
  write_capacity   = "${var.wcus}"
  stream_enabled   = true
  stream_view_type = "NEW_IMAGE"
  hash_key         = "UserId"
  range_key        = "Id"

  attribute {
    name = "UserId"
    type = "S"
  }

  attribute {
    name = "Id"
    type = "S"
  }
}
