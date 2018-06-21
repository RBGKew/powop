import axios from 'axios'

const url = 'http://localhost:10080/harvester/api/1/'
const api = axios.create({
  baseURL: url
})

const organisations = function() {
  return api.get('organisation')
}

const organisation = function(identifier) {
  return api.get('organisation/' + identifier)
}

export default {
  organisations,
  organisation
}
