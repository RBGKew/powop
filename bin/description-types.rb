#!/usr/bin/ruby

filename = ARGV.shift

puts "reading from #{filename}"
File.readlines(filename).each do |line|
  line.strip!
#  tokens = line.split(':')
#  enumName = tokens.last
#
#  if %w[male female].include?(enumName)
#    enumName += tokens[-2].capitalize
#  end
#
  enumName = line.gsub(/-(.)/) {$1.capitalize}.gsub(/\s*:\s*(.)/) {$1.capitalize}

  puts %|#{enumName}("#{line}"),|
end
