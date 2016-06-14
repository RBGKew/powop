#!/usr/bin/ruby

filename = ARGV.shift

original = []
enum = []
display = []
File.readlines(filename).each do |line|
  line.strip!
  e, d = line.split(',')
  enum.push  e.gsub(/-(.)/) {$1.capitalize}.gsub(/\s*:\s*(.)/) {$1.capitalize}
  display.push d
  original.push e
end

enum.each_with_index { |e, i|  puts %|#{e}("#{original[i]}"),| }

puts "\n\n-------------------------------------------------------\n\n"

display.each_with_index { |d, i|  puts %|#{enum[i]}=#{d}| }
