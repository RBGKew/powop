#!/usr/bin/ruby

filename = ARGV.shift

original = []
enum = []
display = []
group = Hash.new([])

File.readlines(filename).each do |line|
  line.strip!
  e, d, g = line.split(',').map(&:strip)
  enum_name = e.gsub(/-(.)/) {$1.capitalize}.gsub(/\s*:\s*(.)/) {$1.capitalize}
  enum.push enum_name
  display.push d
  original.push e
  group[g] += [enum_name]
end

enum.each_with_index { |e, i|  puts %|#{e}("#{original[i]}"),| }

puts "\n\n-------------------------------------------------------\n\n"

display.each_with_index { |d, i|  puts %|#{enum[i]}=#{d}| }

puts "\n\n-------------------------------------------------------\n\n"

group.each do |group_name, list|
  puts %{.put("#{group_name}", ImmutableSet.<DescriptionType>of(}
  list.each_with_index do |dt, i|
    if i == (list.length - 1)
      puts "\t\t#{dt}))"
    else
      puts "\t\t#{dt},"
    end
  end
end
