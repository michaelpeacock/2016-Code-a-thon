#!/usr/bin/env ruby
# simple util to convert an ais csv to point shapefile - jay@alaska.edu
#
require "georuby"
require "geo_ruby/shp"
include GeoRuby::Shp4r
include GeoRuby::SimpleFeatures
require "trollop"
require "pp"
require "csv"
require "date"


HEADER_MAPPER = { "UTC Time" => 0,
 "Local Time" => 1,
 "MMSI" => 2,
 "IMO" => 3,
 "Name" => 4,
 "Type" => 5,
 "Length" => 6,
 "Beam" => 7,
 "Draught" => 8,
 "SOG" => 9,
 "COG" => 10,
 "Latitude"=>11,
 "Longitude"=>12,
 "Destination"=>13,
 "Status"=>14,
 "Flag"=>15}

def line_to_hash(item) 
	line = {}
	HEADER_MAPPER.each do |key,index|
		line[key] = item[index]	
	end	
	#8/28/16 3:09
	#pp line["UTC Time"]
	line["UTC Time (tm)"] = DateTime.strptime(line["UTC Time"], "%m/%d/%y %H:%M")
	line
end

def get_point(item)
	Point.from_coordinates([item["Longitude"].to_f,item["Latitude"].to_f],4326)
end

## Command line parsing action..
parser = Trollop::Parser.new do
  version "0.0.1 jay@alaska.edu"
  banner <<-EOS
  This util takes ais CSV files and turns them into shapefiles with points. 

Usage:
      ais_csv_to_line.rb  <csv file> <shapefile with lines>
where [options] is:
EOS

end

opts = Trollop::with_standard_exception_handling(parser) do
  o = parser.parse ARGV
  raise Trollop::HelpNeeded if ARGV.length == 0 # show help screen
  o
end


ships = []

CSV.foreach(ARGV[0]) do |row|
	next if row[0] == "UTC Time"
	ships << line_to_hash(row)
end

shape_fields = []

HEADER_MAPPER.keys.each do |field|
	shape_fields << DBF::Field.new(field, "C", 40)
end

shpfile = ShpFile.create(ARGV[1],ShpType::POINT,shape_fields)
shpfile.transaction do |tr|
	ships.each do |ship|
               tr.add(ShpRecord.new(get_point(ship),ship))
        end
end

