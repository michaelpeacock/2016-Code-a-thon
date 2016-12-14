#!/usr/bin/env ruby
#simple script to rename mirs npp data to scheme for asrc fed. 
require "date"


DATE_FORMAT = "%Y%m%d"

def get_date(t) 
	#npp.16158.0952.prj
	#0      1   2   3  4123456 5
	#masie_ice_r00_v01_2016149_1km.shx
	bits = File.basename(t).split("_")
	bits[0,4].join(".") + "." + DateTime.ordinal(bits[4][0,4].to_i, bits[4][4,3].to_i,0,0).strftime(DATE_FORMAT) + "." + bits.last
end


ARGV.each do |item|
	system("ln -s #{item} #{get_date(item)}")
end
