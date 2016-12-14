#!/usr/bin/env ruby
#simple script to rename mirs npp data to scheme for asrc fed. 
require "date"


DATE_FORMAT = "%Y%m%d.%H%M"

def get_date(t) 
	#npp.16158.0952.prj
	bits = File.basename(t).split(".")
	"mirs." + bits.first + "." + DateTime.ordinal(2000+bits[1][0,2].to_i, bits[1][2,3].to_i, bits[2][0,2].to_i,  bits[2][3,2].to_i).strftime(DATE_FORMAT) + "." + bits.last
end


ARGV.each do |item|
	system("git mv #{item} #{get_date(item)}")
end
