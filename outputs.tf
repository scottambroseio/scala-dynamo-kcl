output "arn" {
  value = "${aws_dynamodb_table.table.arn}"
}

output "id" {
  value = "${aws_dynamodb_table.table.id}"
}

output "stream_arn" {
  value = "${aws_dynamodb_table.table.stream_arn}"
}

output "stream_label" {
  value = "${aws_dynamodb_table.table.stream_label}"
}
