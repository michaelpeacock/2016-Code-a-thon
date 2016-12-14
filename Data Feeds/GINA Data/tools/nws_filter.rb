#!/usr/bin/env ruby
# A very simple utility to filter the NWS's shapefiles to just include ice data. 
ARGV.each do |item|
	filtered = File.basename(item, ".shp") + ".filtered.shp"
	if ( File.exist?(filtered) )
		puts("INFO: skipping")
		next
	end
	system("ogr2ogr -where \"POLY_TYPE='I'\" #{filtered} #{item}")
end
