#!/usr/bin/env ruby
# simple util to convert an ais csv to line shapefile sorted by time, to show motion. jay@alaska.edu
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

SHAPE_HEADERS = [ "MMSI","IMO","Name","Type","Length", "Beam","Draught","SOG","COG","FLAG"]


NAME_KEY = "Name"

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

def get_line(items)
	line = []
	items.each do |item|
		line << [item["Longitude"].to_f,item["Latitude"].to_f]
	end
	LineString.from_coordinates(line,4326)
end

def get_data(ship)
	fields = {}

	SHAPE_HEADERS.each do |x|
		fields[x] = ship[0][x] 
	end
	fields
end


## Command line parsing action..
parser = Trollop::Parser.new do
  version "0.0.1 jay@alaska.edu"
  banner <<-EOS
  This util takes ais CSV files and turns them into shapefiles with lines, to show ship motion. 

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


ships = {} 



CSV.foreach(ARGV[0]) do |row|
	next if row[0] == "UTC Time"
	item = line_to_hash(row)
	ships[item[NAME_KEY]] = [] if !ships[item[NAME_KEY]]
	ships[item[NAME_KEY]] << item
end


#["CHAMPION"].each do |ship|
ships.keys.sort.each do |ship|
	ships[ship].sort! { |x,y| x["UTC Time (tm)"] <=> y["UTC Time (tm)"]}
	get_line(ships[ship])
end

shape_fields = []

SHAPE_HEADERS.each do |field|
	shape_fields << DBF::Field.new(field, "C", 200)
end

#exit
#shpfile = GeoRuby::Shp4r::ShpFile.create('hello.shp', ShpType::POINT, [DBF::Field.new("Hoyoyo", "C", 10), DBF::Field.new("Boyoul","N",10,0)])


shpfile = ShpFile.create(ARGV[1],ShpType::POLYLINE,shape_fields)
shpfile.transaction do |tr|
	ships.keys.each do |ship|
	       data = get_data(ships[ship])
	       #pp data
               tr.add(ShpRecord.new(get_line(ships[ship]),get_data(ships[ship])))
        end
end

